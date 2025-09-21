package dev.sajid.backend.services;

import java.util.HashMap;
import java.util.Map;

public class BranchNamer {
    public static Map<Integer, String[]> branchName = new HashMap<>();
    static {
        branchName.put(1, new String[]{"CSE", "Computer Science & Engineering"});
        branchName.put(2, new String[]{"CIV", "Civil Engineering"});
        branchName.put(3, new String[]{"CSE", "Computer Science & Technology"});
        branchName.put(4, new String[]{"CSE", "Electronics and Communication Engineering"});
        branchName.put(5, new String[]{"CSE", "Mechanical Engineering"});
        branchName.put(6, new String[]{"CSE", "Computer Science & Business Systems"});
        branchName.put(7, new String[]{"CSE", "Electrical and Electronics Engineering"});
        branchName.put(8, new String[]{"CSE", "Data Science"});
        branchName.put(9, new String[]{"CSE", "Artificial Intelligence & Machine Learning"});
    }

    public static String getBranchShortNameByCode(int code){
        if (!branchName.containsKey(code)){
            throw new RuntimeException("No branch exists with branch code = "+code);
        }
        return branchName.get(code)[0];
    }

    public static String getBranchFullNameByCode(int code){
        if (!branchName.containsKey(code)){
            throw new RuntimeException("No branch exists with branch code = "+code);
        }
        return branchName.get(code)[1];
    }

    public static String[] getBranchNameByCode(int code){
        if (!branchName.containsKey(code)){
            throw new RuntimeException("No branch exists with branch code = "+code);
        }
        return branchName.get(code);
    }
}
