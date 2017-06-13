package com.seventh7.mybatis.setting;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.seventh7.mybatis.generate.GenerateModel;
import com.seventh7.mybatis.generate.StatementGenerator;
import com.seventh7.mybatis.ref.license.LicenseData;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

/**
 * Created by CrazyBunQnQ on 2017/6/13.
 */
@State(
        name = "MybatisSettings",
        storages = {@Storage(
                id = "other",
                file = "$APP_CONFIG$/mybatis.xml"
        )}
)
public class MybatisSetting implements PersistentStateComponent<Element> {
    private static final String STATEMENT_GENERATE_MODEL_SETTING_ID = "StatementGenerateModel";
    private static final String DEFAULT_SOURCE_SETTING_ID = "DefaultDataSourceId";
    private static final Type SETTING_TYPE_TOKEN = (new TypeToken<Set<String>>() {
    }).getType();
    private GenerateModel statementGenerateModel;
    private String defaultDataSourceId = "";
    private String key;
    private String result;
    private Gson gson = new Gson();
    private int insert;
    private int select;
    private int update;
    private int delete;
    private Set<String> customPluginUrls = Sets.newHashSet();

    public MybatisSetting() {
        this.statementGenerateModel = GenerateModel.START_WITH_MODEL;
    }

    public static MybatisSetting getInstance() {
        return (MybatisSetting)ServiceManager.getService(MybatisSetting.class);
    }

    @Nullable
    public Element getState() {
        Element element = new Element("MybatisSettings");
        element.setAttribute("CUSTOM_PLUGIN", this.gson.toJson(this.customPluginUrls));
        element.setAttribute("KEY", this.getKey());
        element.setAttribute("RESULT", this.getResult());
        element.setAttribute(StatementGenerator.INSERT_GENERATOR.getId(), this.gson.toJson(StatementGenerator.INSERT_GENERATOR.getPatterns()));
        element.setAttribute(StatementGenerator.DELETE_GENERATOR.getId(), this.gson.toJson(StatementGenerator.DELETE_GENERATOR.getPatterns()));
        element.setAttribute(StatementGenerator.UPDATE_GENERATOR.getId(), this.gson.toJson(StatementGenerator.UPDATE_GENERATOR.getPatterns()));
        element.setAttribute(StatementGenerator.SELECT_GENERATOR.getId(), this.gson.toJson(StatementGenerator.SELECT_GENERATOR.getPatterns()));
        element.setAttribute("StatementGenerateModel", String.valueOf(this.statementGenerateModel.getIdentifier()));
        this.setDefaultDataSourceIdIfNull();
        element.setAttribute("DefaultDataSourceId", this.defaultDataSourceId);
        element.setAttribute("Insert", String.valueOf(this.insert));
        element.setAttribute("Select", String.valueOf(this.select));
        element.setAttribute("Update", String.valueOf(this.update));
        element.setAttribute("Delete", String.valueOf(this.delete));
        return element;
    }

    private void setDefaultDataSourceIdIfNull() {
        if(this.defaultDataSourceId == null) {
            this.defaultDataSourceId = "";
        }

    }

    public void loadState(Element state) {
        this.setCustomPluginUrl(state.getAttributeValue("CUSTOM_PLUGIN"));
        this.setKey(state.getAttributeValue("KEY"));
        this.setResult(state.getAttributeValue("RESULT"));
        this.loadState(state, StatementGenerator.INSERT_GENERATOR);
        this.loadState(state, StatementGenerator.DELETE_GENERATOR);
        this.loadState(state, StatementGenerator.UPDATE_GENERATOR);
        this.loadState(state, StatementGenerator.SELECT_GENERATOR);
        String model = state.getAttributeValue("StatementGenerateModel");
        this.statementGenerateModel = GenerateModel.getInstance(model);
        this.defaultDataSourceId = state.getAttributeValue("DefaultDataSourceId");
        String insert = state.getAttributeValue("Insert");
        this.insert = StringUtils.isBlank(insert)?1:Integer.valueOf(insert).intValue();
        this.setDefaultDataSourceIdIfNull();
    }

    private void setCustomPluginUrl(String customPlugin) {
        if(StringUtils.isBlank(customPlugin)) {
            this.customPluginUrls = Sets.newHashSet();
        } else {
            this.customPluginUrls = (Set)this.gson.fromJson(customPlugin, (new TypeToken<Set<String>>() {
            }).getType());
            this.customPluginUrls = Sets.newHashSet(this.customPluginUrls);
        }
    }

    public String getDefaultDataSourceId() {
        return this.defaultDataSourceId;
    }

    public void setDefaultDataSourceId(String defaultDataSourceId) {
        this.defaultDataSourceId = defaultDataSourceId;
    }

    private void loadState(Element state, StatementGenerator generator) {
        String attribute = state.getAttributeValue(generator.getId());
        if(null != attribute) {
            generator.setPatterns((Set)this.gson.fromJson(attribute, SETTING_TYPE_TOKEN));
        }

    }

    public String getKey() {
        return this.key == null?"":this.key;
    }

    public void setKey(String key) {
        this.key = "305c300d06092a864886f70d0101010500034b003048024100958ac1b5552c3eece860d8c0989824c20bedacc78b7f8d6ab5ca80617f8bdbb279524305a7ca6c7de7b006b91e56892a35b2223b99504b3e5c1512bb48ce92b90203010001";
        if(StringUtils.isBlank(this.key)) {
            ++this.insert;
        }

    }

    public String getResult() {
        return this.result == null?"":this.result;
    }

    public void setResult(String result) {
        this.result = "03871b42c83e53e109b239851af6c68749390606d01f85b001c288876d5544ca7813538e1c657902f2622c95703439ac2bcbb97249e2a3ce3c5f4b7fd976c719";
    }

    public GenerateModel getStatementGenerateModel() {
        return this.statementGenerateModel;
    }

    public void setStatementGenerateModel(GenerateModel statementGenerateModel) {
        this.statementGenerateModel = statementGenerateModel;
    }

    public void setLicenseData(LicenseData licenseData) throws IOException {
        getInstance().setKey(licenseData.getKey());
        getInstance().setResult(licenseData.getResult());
    }

    public Set<URL> getCustomPluginUrls() {
        HashSet<URL> urls = Sets.newHashSet();
        Iterator iterator = this.customPluginUrls.iterator();

        while(iterator.hasNext()) {
            String url = (String)iterator.next();

            try {
                urls.add(new URL(url));
            } catch (MalformedURLException var5) {
                iterator.remove();
            }
        }

        return urls;
    }

    public void addCustomPluginUrl(URL url) {
        if(url != null) {
            this.customPluginUrls.add(url.toString());
        }
    }

    public void clearCustomPluginDependencies() {
        this.customPluginUrls = Sets.newHashSet();
    }
}

