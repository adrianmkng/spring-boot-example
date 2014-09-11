package com.codeng.springboot.provider;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class JdbcUserDetailsManagerProvider implements FactoryBean<JdbcUserDetailsManager> {

    @Inject
    private DataSource dataSource;

    @Override
    public JdbcUserDetailsManager getObject() throws Exception {
        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
        userDetailsService.setDataSource(dataSource);
        return userDetailsService;
    }

    @Override
    public Class<JdbcUserDetailsManager> getObjectType() {
        return JdbcUserDetailsManager.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
