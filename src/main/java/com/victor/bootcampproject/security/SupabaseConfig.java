package com.victor.bootcampproject.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SupabaseConfig {
    private static String PROJECT = "nltqgtftdcbzdbgubmoa";
    public static String
            SUPABASE_URL = "https://" + PROJECT + ".supabase.co",
            SUPABASE_JWKS_URl = SUPABASE_URL + "/auth/v1/.well-known/jwks.json",
            SUPABASE_API_KEY =          "sb_publishable_bc_6MUmplE8MJ16VQvwHlA_Jk3F8ogx",
            SUPABASE_publishable_KEY =  "sb_publishable_bc_6MUmpIe8MJ16VQvwHLA_Jk3F8ogx",
            SUPABASE_service_Role_Key = "sb_secret_W4sOlOu7xXRKbLniDtRUoA_3omIpv7G",
            SUPABASE_ANON_KEY =          "sb_secret_W4sOlOu7xXRKbLniDtRUoA_3omIpv7G";
}