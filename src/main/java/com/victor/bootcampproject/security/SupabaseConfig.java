package com.victor.bootcampproject.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SupabaseConfig {
    public String url = "https://nltqgtftdcbzdbgubmoa.supabase.co";
    public String apikey = "sb_publishable_bc_6MUmplE8MJ16VQvwHlA_Jk3F8ogx";
    public String publishableKey = "sb_publishable_bc_6MUmpIe8MJ16VQvwHLA_Jk3F8ogx";
    public String serviceRoleKey = "sb_secret_W4sOlOu7xXRKbLniDtRUoA_3omIpv7G";
    public String jwksUrl = "https://nltqgtftdcbzdbgubmoa.supabase.co/auth/v1/jwks";
    public String anonKey = serviceRoleKey;
}
