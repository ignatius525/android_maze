package edu.wm.cs.cs301.IgnatMiagkov;

import edu.wm.cs.cs301.IgnatMiagkov.generation.MazeBuilder;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Order;

public class OrderHolder {

    private static int skillLevel;
    private static Order.Builder builder = Order.Builder.DFS;
    private static boolean toRevisit = false;

    public static int getSkillLevel(){
        return skillLevel;
    }

    public static void setSkillLevel(int diff){
        skillLevel = diff;
    }

    public static Order.Builder getBuilder(){
        return builder;
    }

    public static void setBuilder(Order.Builder build){
        builder = build;
    }

    public static boolean getRevisit(){
        return toRevisit;
    }

    public static void setRevisit(boolean bool){
        toRevisit = bool;
    }
}
