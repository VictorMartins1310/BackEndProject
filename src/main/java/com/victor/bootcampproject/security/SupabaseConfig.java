package com.victor.bootcampproject.security;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "supabase") // reminder: is for getting supabase data from yaml files
public class SupabaseConfig {
    public String project, SUPABASE_URL, SUPABASE_JWKS_URl;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
        SUPABASE_URL = "https://" + project + ".supabase.co";
        SUPABASE_JWKS_URl = SUPABASE_URL + "/auth/v1/.well-known/jwks.json";
    }
}