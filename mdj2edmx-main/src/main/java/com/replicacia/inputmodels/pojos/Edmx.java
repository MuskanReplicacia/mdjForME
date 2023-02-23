package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "edmx:Edmx")
@XmlAccessorType(XmlAccessType.FIELD)
public class Edmx {

    @XmlAttribute(name = "Version")
    private String version;

    @XmlElement(name = "edmx:DataServices")
    private DataServices dataServices;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DataServices getDataServices() {
        return dataServices;
    }

    public void setDataServices(DataServices dataServices) {
        this.dataServices = dataServices;
    }
}
