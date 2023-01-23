package ru.microhost.hoster.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("storage")
public class StorageProperties {
    private String location;

    public StorageProperties(){
        location = "/home/podliva/IdeaProjects/Hoster/uploads/";
    }
    public StorageProperties(String currentUser){
        location = "/home/podliva/IdeaProjects/Hoster/uploads" + "/" + currentUser;
    }

    public String getLocation() {
        return location;
    }

}

