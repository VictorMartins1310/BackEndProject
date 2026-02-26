package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.model.Frequency;
import com.victor.bootcampproject.model.ProductType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class WebControl {
    /**
     * Forwards all non-API and non-static asset requests to the index.html file.
     * 
     * This method handles client-side routing for single-page applications (SPA) by
     * redirecting any GET request that doesn't match the excluded patterns (api, assets,
     * or index.html itself) to the index.html entry point.
     * 
     * The path pattern uses a negative lookahead regex to exclude:
     * - Requests starting with '/api' (API endpoints)
     * - Requests starting with '/assets' (static resources)
     * - Direct requests to '/index.html'
     * 
     * @return a forward directive to /index.html, allowing the client-side router
     *         to handle the routing
     */
    @GetMapping(value = { "/{path:^(?!api|assets|index\\.html$).*$}"})
    public String forward() {
        return "forward:/index.html";
    }
}