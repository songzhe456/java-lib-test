package com.lib.util;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.lwjgl.system.MemoryUtil;

public class ArgumentParser {
    private static boolean redRequested = false;

    public void parseArgs(String[] args){
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("need-me");
        parser.accepts("R");
        parser.accepts("crash");
        OptionSet options = parser.parse(args);

        if(options.has("need-me")){
            System.out.println("你需要我？");
        }
        if(options.has("R")){
            setRedRequested(true);
        }
        if (options.has("crash")) {
            MemoryUtil.memSet(0L,0,1L);
        }
    }

    public static boolean isRedRequested() {
        return redRequested;
    }

    public static void setRedRequested(boolean value){
        redRequested = value;
    }
}
