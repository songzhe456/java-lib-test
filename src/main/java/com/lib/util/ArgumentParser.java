package com.lib.util;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ArgumentParser {
    public void parseArgs(String[] args){
        OptionParser parser = new OptionParser();
        parser.accepts("need-me");
        OptionSet options = parser.parse(args);

        if(options.has("need-me")){
            System.out.println("你需要我？");
        }
    }
}
