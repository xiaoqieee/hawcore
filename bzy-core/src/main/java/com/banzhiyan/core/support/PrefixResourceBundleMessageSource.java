package com.banzhiyan.core.support;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.*;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class PrefixResourceBundleMessageSource extends ResourceBundleMessageSource {
    private static List<String> localeSuffixes;
    private Set<String> basenames;

    public PrefixResourceBundleMessageSource() {
    }

    public Set<String> getBasenames() {
        return this.basenames;
    }

    @Required
    public void setLocationPatterns(String[] locationPatterns) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getBundleClassLoader());
        this.basenames = new HashSet();
        String[] arr$ = locationPatterns;
        int len$ = locationPatterns.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String locationPattern = arr$[i$];
            int lastIndexOf = locationPattern.lastIndexOf(47);
            String prefix;
            if(lastIndexOf == -1) {
                prefix = "";
            } else {
                prefix = locationPattern.substring(locationPattern.indexOf(58) + 1, lastIndexOf + 1);
            }

            Resource[] messageResources;
            try {
                messageResources = resolver.getResources(locationPattern);
            } catch (IOException var17) {
                throw new IllegalArgumentException("Can't get resources[" + locationPattern + "].", var17);
            }

            Resource[] arr = messageResources;
            int len = messageResources.length;

            for(int i = 0; i < len; ++i) {
                Resource resource = arr[i];
                String resourceName = prefix + resource.getFilename();
                Iterator iterator = localeSuffixes.iterator();

                while(iterator.hasNext()) {
                    String localeSuffix = (String)iterator.next();
                    if(resourceName.endsWith(localeSuffix)) {
                        this.basenames.add(resourceName.substring(0, resourceName.lastIndexOf(localeSuffix)));
                    }
                }
            }
        }

        this.setBasenames((String[])this.basenames.toArray(new String[this.basenames.size()]));
    }

    public List<ResourceBundle> getResourceBundles(Locale locale) {
        List<ResourceBundle> rbs = new LinkedList();
        Iterator i$ = this.basenames.iterator();

        while(i$.hasNext()) {
            String basename = (String)i$.next();
            ResourceBundle bundle = this.getResourceBundle(basename, locale);
            if(bundle != null) {
                rbs.add(bundle);
            }
        }

        return rbs;
    }

    static {
        Locale[] availableLocales = Locale.getAvailableLocales();
        localeSuffixes = new ArrayList(availableLocales.length);
        Locale[] arr$ = availableLocales;
        int len$ = availableLocales.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Locale availableLocale = arr$[i$];
            localeSuffixes.add("_" + availableLocale.toString() + ".properties");
        }

    }
}

