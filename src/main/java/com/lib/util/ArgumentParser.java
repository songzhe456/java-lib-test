package com.lib.util;

import com.lib.App;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ArgumentParser {
    private static final boolean DO_NOT_CLEAR_COLOR = true;
    public void parseArgs(String[] args){
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("need-me");
        parser.accepts("R");
        OptionSet options = parser.parse(args);

        if(options.has("need-me")){
            System.out.println("你需要我？");
        }

        if(options.has("R")){
            App.init();
            App.changeColor(255,0,0,1);
            App.setDoNotClearColor(true);
        }
    }
}
