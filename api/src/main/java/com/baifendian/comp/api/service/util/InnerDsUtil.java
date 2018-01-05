package com.baifendian.comp.api.service.util;

import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.common.ds.DsConfUtil;
import com.baifendian.comp.dao.postgresql.mapper.DsDirRefMapper;
import com.baifendian.comp.dao.postgresql.model.ds.DsDirRef;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InnerDsUtil {
  @Autowired
  private DsDirRefMapper dsDirRefMapper;

  private Map<String, String> createDsDirRef(String userId) {
    List<DsDirRef> dsDirRefList = dsDirRefMapper.selectByUser(userId);

    Map<String, String> dsDirRefMap = new HashMap<>();
    if (CollectionUtils.isNotEmpty(dsDirRefList)) {
      dsDirRefMap = dsDirRefList.stream()
          .collect(Collectors.toMap(DsDirRef::getDirId, DsDirRef::getDsId));
    }

    return dsDirRefMap;
  }

  public void checkDsDirName(String userId, String name){
    List<String> innerDsNames = getInnerDsName(userId);

    if (innerDsNames.stream().anyMatch(name::equals)){
      throw new PreFailedException("com.baifendian.bi.api.common.duplicate", I18nUtil.dirName());
    }
  }

  public List<String> getInnerDsName(String userId){
    List<DsDirRef> dsDirRefList = dsDirRefMapper.selectByUser(userId);
    List<String> names = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(dsDirRefList)) {
      names = dsDirRefList.stream()
          .map(df -> DsConfUtil.getDsName(df.getDsId()))
          .collect(Collectors.toList());
    }

    return names;
  }

  public List<DsDirRef> getInnerDs(String userId){
    return dsDirRefMapper.selectByUser(userId);
  }
}
