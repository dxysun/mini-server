package com.mini10.miniserver.model.param;

import com.mini10.miniserver.model.ConditionTag;
import com.mini10.miniserver.model.RequirementTag;

/**
 * @Author ChenShaoJie
 * @Date 2019-07-24 16:25
 * 两个标签的相似度的结果存放类
 */
public class MatchTagSimilarity {
    private RequirementTag requirementTag; //需求标签
    private ConditionTag conditionTag; //条件标签
    private double similarity; //两个标签的相似度

    public MatchTagSimilarity() {
    }

    public MatchTagSimilarity(RequirementTag requirementTag, ConditionTag conditionTag, double similarity) {
        this.requirementTag = requirementTag;
        this.conditionTag = conditionTag;
        this.similarity = similarity;
    }

    public RequirementTag getRequirementTag() {
        return requirementTag;
    }

    public void setRequirementTag(RequirementTag requirementTag) {
        this.requirementTag = requirementTag;
    }

    public ConditionTag getConditionTag() {
        return conditionTag;
    }

    public void setConditionTag(ConditionTag conditionTag) {
        this.conditionTag = conditionTag;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
