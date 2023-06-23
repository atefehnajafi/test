package com.neshan.springh2project.config;
import java.util.Collections;
import java.util.List;

public abstract class LoadBalancer {
    final java.util.List<String> ipList;

    public LoadBalancer(List<String> ipList) {
        this.ipList = Collections.unmodifiableList(ipList);
    }

    public abstract String getIp();
}
