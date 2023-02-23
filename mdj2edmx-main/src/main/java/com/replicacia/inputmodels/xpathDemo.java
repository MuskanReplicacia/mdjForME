package com.replicacia.inputmodels;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replicacia.inputmodels.pojos.*;

import org.atteo.evo.inflector.English;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class xpathDemo {
    public xpathDemo() {
        super();
    }

    public static void main(String[] args) throws Exception {

        read();
        withJsonPath();
    }



    public static void read() {

        String filename = "Product_Categories_Star_UML.mdj";
        System.out.println("in read");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e1) {

        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(inputStream);
            System.out.println(node.toString());
            if (node.has("ownedElements")) {
                System.out.println("keys");
            }
            List<JsonNode> keys = node.findValues("ownedElements");
            System.out.println("keys" + keys.size());
            for (JsonNode cnode : keys) {
                System.out.println(cnode.toString());
                //                         if(cnode.get("_type").equals("UMLModel")){
                //                             System.out.println(cnode.get("_type"));
                //                         }
                List<JsonNode> elements = cnode.findValues("ownedElements");

                System.out.println("elements " + elements.size());
                for (JsonNode cnode1 : elements) {
                    System.out.println(cnode1.get("_type"));
                    System.out.println(cnode1.toString());

                }

            }
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
//"ecom_basic_model_copy.mdj";//
    public static void withJsonPath() {
        String filename = "Product_Categories_Star_UML.mdj";
        ObjectMapper mapper = new ObjectMapper();
        List<EntityType> entityList = null;
        Map<String,String> classRefMap =null;
        try {
            JsonNode projectNode = mapper.readTree(new File(filename));
            JsonNode childrenNodes = projectNode.path("ownedElements")
                                                .path(0)
                                                .path("ownedElements");
            entityList = new ArrayList<EntityType>();
            classRefMap = new HashMap<String,String>();
            if (childrenNodes.isArray()) {
                for (JsonNode node : childrenNodes) {
                    System.out.println("Node type inner 3333 : " + node.path("_type").toString());
                    String type = node.path("_type").asText();
                    System.out.println("type:: " + type);
                    
                    if ("UMLClass".equalsIgnoreCase(type)) {
                        String refId = node.path("_id").asText();
                        String className =  node.path("name").asText();
                        System.out.println("refId ::" +refId +"className::"+ className);
                        classRefMap.put(refId,className);
                        EntityType eType = (EntityType) getUmlClassObject(node);
                        entityList.add(eType);
                    }
                }
                System.out.println(entityList.toString());
            }
            List<EntitySet> eSetList = new ArrayList<EntitySet>();
            Map entityMap = setNavigations(entityList, projectNode, eSetList,classRefMap);

            writeToXml((List<EntityType>) entityMap.get("EntityType"), (List<EntitySet>) entityMap.get("EntitySet"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Object getUmlClassObject(JsonNode node) {

        System.out.println("Node type inner 111: type " + node.path("_type").toString());
        System.out.println("Node type inner 111: name " + node.path("name").toString());

        EntityType eType = new EntityType();
        eType.setName(node.path("name").asText());
        JsonNode attrnodes = node.path("attributes");
        List<Property> proList = null;
       
        if (attrnodes.isArray()) {
            proList = new ArrayList<Property>();
            for (JsonNode atrnode : attrnodes) {
                Property property = new Property();
                String proid = atrnode.path("name").asText();
                property.setName(atrnode.path("name").asText());
                property.setType(atrnode.path("type").asText());
                System.out.println("property name :: " + atrnode.path("name").asText());                
                System.out.println("property id :: " + atrnode.path("isID").asBoolean());
                if (atrnode.path("isID").asBoolean() ) {
                    PropertyRef propertyRef = new PropertyRef();
                    propertyRef.setName(proid);
                    Key key = new Key();
                    key.setPropertyRef(propertyRef);
                    eType.setKey(key);
                }
                proList.add(property);
            }
        }
        eType.setProperties(proList);

        return eType;
    }

    public static Map setNavigations(List<EntityType> etList, JsonNode projectnode,
                                     List<EntitySet> eSetList, Map<String, String> classRefMap) throws Exception {

        JsonNode childrenNodes = projectnode.path("ownedElements")
                                            .path(0)
                                            .path("ownedElements");

        if (childrenNodes.isArray()) {
            childrenNodes.size();
        }
        Map etAndesMap = null;
        for (JsonNode node : childrenNodes) {
            System.out.println("Node type inner 3333 : " + node.path("_type").toString());
            String type = node.path("_type").asText();
            String cName = node.path("name").asText();
            if ("UMLClass".equalsIgnoreCase(type)) {
                //get associations
                JsonNode assonode = node.path("ownedElements").path(0);
                System.out.println("Association name *****::" + assonode.path("_type"));

                if ("UMLAssociation".equalsIgnoreCase(assonode.path ("_type").asText())) {
                    String name = assonode.path("name").asText();
                    System.out.println("Association name *****::" + assonode.path("name"));
                    //get ends
                    JsonNode end1node = assonode.path("end1");
                    JsonNode end2node = assonode.path("end2");
                    etAndesMap = createNavigations(etList, end1node, end2node, eSetList,classRefMap);

                }
            }
        }


        return etAndesMap;

    }

    public static Map createNavigations(List<EntityType> etList, JsonNode end1node, JsonNode end2node,
                                        List<EntitySet> eSetList, Map<String , String> classRefMap) throws Exception {
        String end1Name = end1node.path("name").asText();
        String end2Name = end2node.path("name").asText();
        
        String ref1 = end1node.path("reference").path("$ref").asText();
        String ref2 = end2node.path("reference").path("$ref").asText();
        
        String multiplicity1 =end1node.path("multiplicity").asText(); 
        String multiplicity2 =end2node.path("multiplicity").asText();
        
        String srcClassName = classRefMap.get(ref1);
        String tarClassName =  classRefMap.get(ref2);
        
        System.out.println(" srcClassName :: " + srcClassName);
        System.out.println("tarClassName :: " +tarClassName);
        
        for (EntityType et : etList) {
            if (et.getName().equalsIgnoreCase(srcClassName)) {
                NavigationProperty nprop = new NavigationProperty();
                nprop.setName(tarClassName);
                nprop.setPartner(srcClassName);
                if(multiplicity2.indexOf("*") > 0 ){
                    nprop.setType("Collection("+tarClassName+")");
                }else{
                    nprop.setType(tarClassName);
                }
               
//                et.setNavigationProperties(nprop);

                EntitySet eSet = new EntitySet();
                eSet.setName(English.plural(srcClassName)); // "set plural form of srcClassName"
                NavigationPropertyBinding nproBind = new NavigationPropertyBinding();
                nproBind.setPath(tarClassName);
                nproBind.setTarget(English.plural(tarClassName)); //"set plural form of tar class name");
//                eSet.setNavigationPropertyBinding(nproBind);

                eSetList.add(eSet);
            }
            if (et.getName().equalsIgnoreCase(tarClassName)) {
                NavigationProperty nprop = new NavigationProperty();
                nprop.setName(srcClassName);
                nprop.setPartner(tarClassName);
                if(multiplicity1.indexOf("*") > 0 ){
                    nprop.setType("Collection("+srcClassName+")");
                }else{
                    nprop.setType(srcClassName);
                }                
//                et.setNavigationProperty(nprop);

                EntitySet eSet = new EntitySet();
                eSet.setName(English.plural(tarClassName)); //"set plural form of tar class name");
                NavigationPropertyBinding nproBind = new NavigationPropertyBinding();
                nproBind.setPath(srcClassName);
                nproBind.setTarget(English.plural(srcClassName)); //"set plural form of src class name");
//                eSet.setNavigationPropertyBinding(nproBind);

                eSetList.add(eSet);
            }
        }
        Map map = new HashMap();
        map.put("EntitySet", eSetList);
        map.put("EntityType", etList);
        return map;
    }


    public static void writeToXml(List<EntityType> etList, List<EntitySet> eset) {
        File f = new File("metadata.xml");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
            Schema sc = new Schema();
            EntityContainer ec = new EntityContainer();
            ec.setName("Container");
            ec.setEntitySet(eset);
            DataServices ds = new DataServices();
            Edmx edmx = new Edmx();

            edmx.setDataServices(ds);
            ds.setSchema(sc);
            sc.setEntityType(etList);
            sc.setEntityContainer(ec);
            JAXBContext contextObj = JAXBContext.newInstance(Edmx.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallerObj.marshal(edmx, new FileOutputStream("metadata.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
