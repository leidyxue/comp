package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.DtoUtil;
import com.baifendian.comp.api.dto.dashboard.DashDetail;
import com.baifendian.comp.api.dto.dashboard.DashUsedTable;
import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.dto.share.DashShareId;
import com.baifendian.comp.api.dto.share.ShareInfo;
import com.baifendian.comp.api.service.util.DashboardUtil;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartNewMapper;
import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.postgresql.model.share.ShareDash;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.postgresql.model.DashAllData;
import com.baifendian.comp.dao.postgresql.mapper.DashShareMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareService {

  @Autowired
  private DashShareMapper dashShareMapper;
  @Autowired
  private DashboardUtil dashUtil;
  @Autowired
  private TableUtilNew tableUtil;
  @Autowired
  private ChartNewMapper chartMapper;

  private ShareDash createShareDash(String shareId) {
    ShareDash shareDash = dashShareMapper.queryById(shareId);
    if (shareDash == null) {
      throw new NotFoundException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.shareName());
    }

    return shareDash;
  }

  public DashShareId dashShare(String userId, String dashID) {
    dashUtil.checkDashboard(userId, dashID);

    String shareId = GenIdUtil.genShareId();

    dashShareMapper.insert(ShareDash.builder().dashId(dashID)
        .id(shareId).build());

    return new DashShareId(shareId);
  }

  public void delShare(String userId, String shareId) {
    ShareDash shareDash = createShareDash(shareId);
    dashUtil.checkDashboard(userId, shareDash.getDashId());

    dashShareMapper.delete(shareId);
  }

  public ShareInfo getDashShare(String userId, String dashId) {
    dashUtil.checkDashboard(userId, dashId);
    List<ShareDash> shareDashList = dashShareMapper.queryByDashId(dashId);

    return new ShareInfo(shareDashList.stream().map(ShareDash::getId).collect(Collectors.toList()));
  }

  public DashDetail getShareDashDetail(String shareId) {
    ShareDash shareDash = createShareDash(shareId);

    DashAllData dashAllData = dashUtil.createDashAll(shareDash.getDashId());

    return DashboardService.createDashDetail(dashAllData);
  }

  public DashUsedTable getDsTables(String shareId) {
    ShareDash shareDash = createShareDash(shareId);

    List<Chart> chartList = chartMapper.findByDId(shareDash.getDashId());

    if (CollectionUtils.isEmpty(chartList)){
      DashUsedTable usedTable = new DashUsedTable();
      usedTable.setDashId(shareDash.getDashId());
      return usedTable;
    }

    Set<String> tableIds = chartList.stream()
        .map(Chart::getTableId)
        .collect(Collectors.toSet());

    return DtoUtil.createDashUsedTable(shareDash.getDashId(), tableUtil.createTables(tableIds));
  }
}
