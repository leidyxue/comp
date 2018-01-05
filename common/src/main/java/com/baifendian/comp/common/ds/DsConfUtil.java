package com.baifendian.comp.common.ds;

import com.baifendian.comp.common.ds.DsMetaConf.DbMeta;
import com.baifendian.comp.common.ds.DsMetaConf.PubDataMeta;
import com.baifendian.comp.common.utils.BaseResponseUtils;
import com.baifendian.comp.common.utils.json.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ResourceUtils;

public class DsConfUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(DsConfUtil.class);

  static private DsMetaConf dsMetaConf = null;
  static private Map<String, PublicDataConf> publicDataConfMap = new HashMap<>();

  static private Map<String, JsonNode> dsNameMap = new HashMap<>();

  static {
    try {
      File dataSourceFile = ResourceUtils.getFile("classpath:ds/ds_meta.json");
      dsMetaConf = JsonUtil.parseObj(FileUtils.readFileToString(dataSourceFile), DsMetaConf.class);
      for (DbMeta dbMeta: dsMetaConf.getDatabase()){
        dsNameMap.put(dbMeta.getDsType().name(), dbMeta.getName());
      }
      for (PubDataMeta pubDataMeta : dsMetaConf.getPubData()) {
        File pubFile = ResourceUtils.getFile("classpath:ds/" + pubDataMeta.getConf());
        PublicDataConf dataConf = new PublicDataConf();
        dataConf.setMeta(JsonUtil.parseList(FileUtils.readFileToString(pubFile)
            , PublicDataConf.PublicTableMeta.class));
        dataConf.setId(pubDataMeta.getId());
        dataConf.setName(pubDataMeta.getName());
        publicDataConfMap
            .put(pubDataMeta.getId(), dataConf);
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  static public DsMetaConf getDsMeta() {
    return dsMetaConf;
  }

  static public PublicDataConf getPubConf(String pubId) {
    return publicDataConfMap.get(pubId);
  }

  static public List<PublicDataConf> getPubConf() {
    return publicDataConfMap.entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
  }

  static public String getDsName(String dsId){
    if (StringUtils.equals(dsId, "SHARE_INNER")){
      return BaseResponseUtils.baseResponseTranslation("com.bfd.bi.common.share.name");
    }
    JsonNode jsonNode = dsNameMap.get(dsId);
    if (jsonNode != null){
      return jsonNode.get(LocaleContextHolder.getLocale().toString()).asText();
    }

    return publicDataConfMap.get(dsId).i18nName();
  }

  static public String getShareDsName(){
    return getDsName("SHARE_INNER");
  }
}
