package com.replicacia.inputmodels;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.replicacia.inputmodels.mappers.ComplexTypeMapper;
import com.replicacia.inputmodels.mappers.EntityTypeMapper;
import com.replicacia.inputmodels.mappers.EnumTypeMapper;
import com.replicacia.inputmodels.pojos.ComplexType;
import com.replicacia.inputmodels.pojos.DataServices;
import com.replicacia.inputmodels.pojos.Edmx;
import com.replicacia.inputmodels.pojos.EntityContainer;
import com.replicacia.inputmodels.pojos.EntitySet;
import com.replicacia.inputmodels.pojos.EntityType;
import com.replicacia.inputmodels.pojos.EnumType;
import com.replicacia.inputmodels.pojos.NavigationProperty;
import com.replicacia.inputmodels.pojos.NavigationPropertyBinding;
import com.replicacia.inputmodels.pojos.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replicacia.util.ExceptionMessages;

import static com.replicacia.inputmodels.mappers.ClassMappers.distributeNodesToTheirStereoTypes;
import static com.replicacia.inputmodels.mappers.ClassMappers.distributeNodesToXmlEntityTheirStereoTypes;

public class MDJToEDMXTransformer implements ConfigurationReader {

	static final Logger log = LogManager.getLogger(MDJ2EDMSTransformer.class.getName());
	
    public Map<String, String> execute(String filename) {
    	String projectName = null; 	//read from root_node/name
    	String modelName = null; 	//
    	
        String schemaName = null; 	//"mycompany.myproject";
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> biDirPartnerMap = new HashMap<>();
        Map<String, String> messages = new HashMap<String, String>();
        try {
            JsonNode projectNode = mapper.readTree(new File(filename));
            projectName = projectNode.path("name").asText();
            modelName = projectNode.path("ownedElements")
                    				.path(0).path("name").asText();
            schemaName  = modelName;
            
            JsonNode childrenNodes = projectNode.path("ownedElements")
                    .path(0)
                    .path("ownedElements");  
            
//            if( schemaName.contentEquals(ExceptionMessages.DEFAULT_SCHEMA_NAME) ) {
//            	messages.put("Missing Schema Name (Optional)", ExceptionMessages.MISSING_SCHEMA_NAME_OPTIONAL);
//            	log.debug("Missing Schema Name (Optional): "+ ExceptionMessages.MISSING_SCHEMA_NAME_OPTIONAL);
//            }
            Map<String, EntityType> entityTypesById = new HashMap<String, EntityType>();
			Map<String, ComplexType> complexTypesById = new HashMap<>();
			Map<String, EnumType> enumTypesById = new HashMap<>();
			distributeNodesToXmlEntityTheirStereoTypes(childrenNodes,entityTypesById, complexTypesById, enumTypesById);
			for (int i = 0; i< childrenNodes.size(); i++) {
				JsonNode classNode = childrenNodes.get(i);
				if ("UMLClass".equalsIgnoreCase(classNode.path("_type").asText())) {
					String id = classNode.path("_id").asText();
					/** Extracts class informat ion and adds to EntityType **/
					String stereoType = classNode.path("stereotype").asText();
					if(Stereotype.ENUMTYPE.getValue().equalsIgnoreCase(stereoType)) {
						EnumTypeMapper.extractEnum(classNode, schemaName, enumTypesById, enumTypesById.get(id), messages);
					} else if(Stereotype.COMPLEXTYPE.getValue().equalsIgnoreCase(stereoType)) {
						ComplexTypeMapper.extractComplexType(classNode, schemaName, complexTypesById, enumTypesById, complexTypesById.get(id));
					} else {
						EntityType eType = entityTypesById.get(id);
						EntityTypeMapper.extractClass(classNode, schemaName, messages,
								entityTypesById, eType, complexTypesById, enumTypesById);
					}

				}

			}
			EntityTypeMapper.extractNavigationPropertyOfBaseEntityForInheritedEntity(schemaName, entityTypesById);
			String finalSchemaName = schemaName;
			List<EntityType> collectionTableEntities = EntityTypeMapper.createCollectionTablesForCollectionTypeFields(entityTypesById, enumTypesById.values().stream().
					collect(Collectors.toMap(enumType -> StringUtils.lowerCase(finalSchemaName + enumType.getName()), enumType -> enumType)), complexTypesById.values().stream().
					collect(Collectors.toMap(complexType -> StringUtils.lowerCase(finalSchemaName + complexType.getName()), complexType -> complexType)), schemaName);
			EntityContainer entityContainer = new EntityContainer();

            List<EntitySet> entitySets = new ArrayList<>();
            for (EntityType entityType : entityTypesById.values()) {
            	EntitySet eSet = getUmlClassEntitySet(entityType, schemaName, 
					 messages);
                entitySets.add( eSet );
            }
			for(EntityType entityType : collectionTableEntities) {
				EntitySet eSet = getUmlClassEntitySet(entityType, schemaName,
					 messages);
				entitySets.add( eSet );
			}

            entityContainer.setName("Container");
            entityContainer.setEntitySet(entitySets);
            
            if( schemaName.contentEquals(ExceptionMessages.DEFAULT_SCHEMA_NAME) ) {
            	messages.put("Missing Schema Name (Optional)", ExceptionMessages.MISSING_SCHEMA_NAME_OPTIONAL);
            }
            Schema schema = new Schema();
	            schema.setNamespace( schemaName );
	            schema.setEntityContainer(entityContainer);
	            schema.setEntityType(new ArrayList<>(entityTypesById.values()));
				schema.getEntityType().addAll(collectionTableEntities);
            	schema.setComplexType(new ArrayList<>(complexTypesById.values()));
				schema.setEnumType(new ArrayList<>(enumTypesById.values()));
            DataServices dataServices = new DataServices();
	        	dataServices.setSchema(schema);

	        Edmx edMx = new Edmx();
	        	edMx.setVersion(ExceptionMessages.EDMX_VERSION);
            	edMx.setDataServices(dataServices);

            EDMXConfigurationWriter writer = new EDMXConfigurationWriter();
            	writer.write(edMx, filename);
            	
        } catch (Exception e) {
        	messages.put("Error", e.getMessage());
        }
        return messages;
    }


	private EntitySet getUmlClassEntitySet(EntityType entityType, String schemaName, Map<String, String> messages) {

        EntitySet entitySet = new EntitySet();
        List<NavigationPropertyBinding> navigationPropertyBindings = null;

        entitySet.setName(entityType.getName() + "s");
        entitySet.setEntityType(schemaName + "." + entityType.getName());

        if(entityType.getNavigationProperties() != null && !entityType.getNavigationProperties().isEmpty()) {
            navigationPropertyBindings = new ArrayList<>();
            for (NavigationProperty navigationProperty : entityType.getNavigationProperties()) {
                NavigationPropertyBinding navigationPropertyBinding = new NavigationPropertyBinding();
                navigationPropertyBinding.setPath(navigationProperty.getName());
                navigationPropertyBinding.setTarget(navigationProperty.getType().
						replace(schemaName + ".", "").
						replace("Collection(", "").
						replace(")", "") + "s");
                navigationPropertyBindings.add(navigationPropertyBinding);
            }
        }

        if(navigationPropertyBindings!= null) {
            entitySet.setNavigationPropertyBinding(navigationPropertyBindings);
        }

        return entitySet;
    }
}
