package com.googlecode.canoe.spring;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.command.CommandHelper;
import com.googlecode.canoe.core.command.support.AbstractCommandMapping;

public class SpringCommandMapping extends AbstractCommandMapping implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringCommandMapping()
    {
        super();
    }

    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;

        init();
    }

    private void init() {
        Map<String, Object> ctrlMap = applicationContext.getBeansWithAnnotation(Controller.class);

        for (String ctrlName : ctrlMap.keySet()) {

            Object ctrl = ctrlMap.get(ctrlName);
            CommandHelper.fetchCommands(ctrl, getCommandEntryMap(), getCallbackEntriesMap());
        }

        commitChanges();
    }
}
