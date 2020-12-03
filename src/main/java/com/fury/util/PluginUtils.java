package com.fury.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Greg on 15/12/2016.
 */
public class PluginUtils {

    public static URLClassLoader loadFromPath(File path) throws MalformedURLException {
        if(path.isDirectory()) {
            return loadFromDirectory(path);
        } else if(path.getName().endsWith(".jar")) {
            return loadFromFile(path);
        }
        return null;
    }

    static URLClassLoader loadFromFile(File file) throws MalformedURLException {
        URL[] urls = new URL[] { file.toURI().toURL() };
        URLClassLoader loader = new URLClassLoader(urls);
        return loader;
    }
    static URLClassLoader loadFromDirectory(File dir) throws MalformedURLException {
        File[] fileList = dir.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
        URL[] urls = new URL[fileList.length];
        for (int i = 0; i < fileList.length; i++)
            urls[i] = fileList[i].toURI().toURL();
        URLClassLoader loader = new URLClassLoader(urls);
        return loader;
    }
}
