package com.xingqiao.system.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;
import com.xingqiao.common.core.domain.IdCardInfo;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.utils.IdCardInfoExtractor;
import com.xingqiao.system.api.domain.AgencyEkyc;
import com.xingqiao.system.api.domain.Qualifications;
import com.xingqiao.system.api.model.SysEmployeeInfoResp;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.api.enums.AuditStatusEnums;
import com.xingqiao.system.api.enums.CertificateFileEnums;
import com.xingqiao.system.api.enums.IdType;
import com.xingqiao.system.domain.SysAudit;
import com.xingqiao.system.domain.SysEmployeeQualifications;
import com.xingqiao.system.domain.SysFile;
import com.xingqiao.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysEmployeeInfoMapper;
import com.xingqiao.system.domain.SysEmployeeInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 员工信息Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@Slf4j
@Service
public class SysEmployeeInfoServiceImpl implements ISysEmployeeInfoService
{
    @Resource
    private SysEmployeeInfoMapper sysEmployeeInfoMapper;
    @Resource
    private ISysAuditService iSysAuditService;
    @Resource
    private ISysFileService iSysFileService;
    @Autowired
    private ISysEmployeeQualificationsService sysEmployeeQualificationsService;
    @Autowired
    private ISysUserService userService;
    /**
     * 查询员工信息
     *
     * @param id 员工信息主键
     * @return 员工信息
     */
    @Override
    public SysEmployeeInfo selectSysEmployeeInfoById(Long id)
    {
        return sysEmployeeInfoMapper.selectSysEmployeeInfoById(id);
    }

    /**
     * 查询员工信息列表
     *
     * @param sysEmployeeInfo 员工信息
     * @return 员工信息
     */
    @Override
    public List<SysEmployeeInfo> selectSysEmployeeInfoList(SysEmployeeInfo sysEmployeeInfo)
    {
        return sysEmployeeInfoMapper.selectSysEmployeeInfoList(sysEmployeeInfo);
    }

    /**
     * 新增员工信息
     *
     * @param sysEmployeeInfo 员工信息
     * @return 结果
     */
    @Override
    public int insertSysEmployeeInfo(SysEmployeeInfo sysEmployeeInfo)
    {
        sysEmployeeInfo.setCreateTime(DateUtils.getNowDate());
        return sysEmployeeInfoMapper.insertSysEmployeeInfo(sysEmployeeInfo);
    }

    /**
     * 修改员工信息
     *
     * @param sysEmployeeInfo 员工信息
     * @return 结果
     */
    @Override
    public int updateSysEmployeeInfo(SysEmployeeInfo sysEmployeeInfo)
    {
        sysEmployeeInfo.setUpdateTime(DateUtils.getNowDate());
        return sysEmployeeInfoMapper.updateSysEmployeeInfo(sysEmployeeInfo);
    }

    /**
     * 批量删除员工信息
     *
     * @param ids 需要删除的员工信息主键
     * @return 结果
     */
    @Override
    public int deleteSysEmployeeInfoByIds(Long[] ids)
    {
        return sysEmployeeInfoMapper.deleteSysEmployeeInfoByIds(ids);
    }

    /**
     * 删除员工信息信息
     *
     * @param id 员工信息主键
     * @return 结果
     */
    @Override
    public int deleteSysEmployeeInfoById(Long id)
    {
        return sysEmployeeInfoMapper.deleteSysEmployeeInfoById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R submitEkycData(AgencyEkyc ekycData) {
        //更新员工信息
        SysEmployeeInfo queryInfo = new SysEmployeeInfo();
        queryInfo.setUserId(ekycData.getUserId());
        List<SysEmployeeInfo> list = sysEmployeeInfoMapper. selectSysEmployeeInfoList(queryInfo);
        SysEmployeeInfo sysEmployeeInfo = setSysEmployeeInfo(ekycData);
        if(!CollectionUtils.isEmpty(list)){
           Long id= list.get(0).getId();
            sysEmployeeInfo.setId(id);
            int update=sysEmployeeInfoMapper.updateSysEmployeeInfo(sysEmployeeInfo);
            log.info("员工信息更新成功 | 员工信息：{}", update);
        }else{
           int save= sysEmployeeInfoMapper.insertSysEmployeeInfo(sysEmployeeInfo);
           log.info("员工信息保存成功 | 员工信息：{}", save);
        }

        Long businessId = sysEmployeeInfo.getId();
        //删除历史资质信息
        int delete=sysEmployeeQualificationsService.deleteByBusinessId(businessId);
        log.info("员工资质信息删除成功 | 员工信息：{}", delete);

        List<SysFile> sysFiles = new ArrayList<>();
        List<Qualifications> qualifications =ekycData.getQualifications();
        Set<Long> qualificationTypes = new HashSet<>();
        for (Qualifications initQualification : qualifications) {
            SysEmployeeQualifications sysEmployeeQualifications = getSysEmployeeQualifications(ekycData, initQualification, businessId);
            int insert=sysEmployeeQualificationsService.insertSysEmployeeQualifications(sysEmployeeQualifications);
            log.info("员工资质信息保存成功 | 员工信息：{}", insert);
            Long qualificationId =sysEmployeeQualifications.getId() ;
            qualificationTypes.add(qualificationId);
            //保存文件路径到系统文件表
            List<String> certificateFileUrls = initQualification.getCertificateFileUrls();
            for (String certificateFileUrl : certificateFileUrls) {
                SysFile sysFile = new SysFile();
                sysFile.setBusinessType(CertificateFileEnums.CERTIFICATE_FILE.getCode());
                setSysFile(ekycData, initQualification, certificateFileUrl, sysFile, qualificationId);
                sysFiles.add(sysFile);
            }
            List<String> practiceCertificateFileUrls = initQualification.getPracticeCertificateFileUrls();
            for (String certificateFileUrl : practiceCertificateFileUrls) {
                SysFile sysFile = new SysFile();
                sysFile.setBusinessType(CertificateFileEnums.PRACTICE_CERTIFICATE_FILE.getCode());
                setSysFile(ekycData, initQualification, certificateFileUrl, sysFile, qualificationId);
                sysFiles.add(sysFile);
            }
        }
        int delete1=iSysFileService.deleteSysFileByBusinessId(qualificationTypes);
        log.info("员工资质文件删除成功 | 员工信息：{}", delete1);
        //批量插入
        int insert=iSysFileService.batchSysFile(sysFiles);
        log.info("员工资质文件保存成功 | 员工信息：{}", insert);
        //更新审核记录
        SysAudit sysAudit = new SysAudit();
        sysAudit.setUserId(ekycData.getUserId());
        sysAudit.setBusinessId(businessId);
        sysAudit.setAuditStatus(AuditStatusEnums.ON.getCode());
        sysAudit.setUpdateBy(ekycData.getUpdateBy());
        sysAudit.setUpdateTime(new Date());
        int update=iSysAuditService.updateSysAudit(sysAudit);
        log.info("员工审核记录更新成功 | 员工信息：{}", update);

        return R.ok();
    }



    private static void setSysFile(AgencyEkyc ekycData, Qualifications initQualification, String certificateFileUrl, SysFile sysFile, Long qualificationId) {
        sysFile.setUserId(ekycData.getUserId());
        sysFile.setBusinessId(qualificationId);
        sysFile.setFileType(initQualification.getQualificationType());
        sysFile.setFileUrl(certificateFileUrl);
        sysFile.setCreateBy(ekycData.getUpdateBy());
        sysFile.setCreateTime(new Date());
        sysFile.setUpdateBy(ekycData.getUpdateBy());
        sysFile.setUpdateTime(new Date());
    }


    private static SysEmployeeQualifications getSysEmployeeQualifications(AgencyEkyc ekycData, Qualifications initQualification, Long businessId) {
        SysEmployeeQualifications sysEmployeeQualifications = new SysEmployeeQualifications();
        sysEmployeeQualifications.setUserId(ekycData.getUserId());
        sysEmployeeQualifications.setUserType("01");
        sysEmployeeQualifications.setEmployeeInfoId(businessId);
        sysEmployeeQualifications.setQualificationType(initQualification.getQualificationType());
        sysEmployeeQualifications.setCertificateNumber(initQualification.getCertificateNumber());
        sysEmployeeQualifications.setIssuingAuthority(initQualification.getIssuingAuthority());
        sysEmployeeQualifications.setCertificateIssueDate(initQualification.getCertificateIssueDate());
        sysEmployeeQualifications.setCertificateExpiryDate(initQualification.getCertificateExpiryDate());
        sysEmployeeQualifications.setYearsOfPractice(initQualification.getYearsOfPractice());
        sysEmployeeQualifications.setProfessionalConfirmed(initQualification.getProfessionalConfirmed());
        sysEmployeeQualifications.setStatus("0");
        sysEmployeeQualifications.setCreateBy(ekycData.getUpdateBy());
        sysEmployeeQualifications.setCreateTime(new Date());
        sysEmployeeQualifications.setUpdateBy(ekycData.getUpdateBy());
        sysEmployeeQualifications.setUpdateTime(new Date());
        return sysEmployeeQualifications;
    }


    private static SysEmployeeInfo setSysEmployeeInfo(AgencyEkyc ekycData) {
        SysEmployeeInfo sysEmployeeInfo = new SysEmployeeInfo();
        sysEmployeeInfo.setUserId(ekycData.getUserId());
        sysEmployeeInfo.setUserType("01");
        sysEmployeeInfo.setIdType(ekycData.getIdType());
        sysEmployeeInfo.setIdNumber(ekycData.getIdNumber());
        if(IdType.ID_CARD.getCode().equals(ekycData.getIdType())){
            IdCardInfo idCardInfo = IdCardInfoExtractor.extractInfo(ekycData.getIdNumber());
            sysEmployeeInfo.setGender(idCardInfo.getGender());
            sysEmployeeInfo.setBirthday(DateUtils.parseDate(idCardInfo.getBirthDate()));
            sysEmployeeInfo.setAge(idCardInfo.getAge().toString());
        }
        sysEmployeeInfo.setIssueDate(ekycData.getIssueDate());
        sysEmployeeInfo.setExpiryDate(ekycData.getExpiryDate());
        sysEmployeeInfo.setFrontIdFileUrl(ekycData.getFrontIdFileUrl());
        sysEmployeeInfo.setBackIdFileUrl(ekycData.getBackIdFileUrl());
        sysEmployeeInfo.setIdentityConfirmed(ekycData.getIdentityConfirmed());
        sysEmployeeInfo.setProfessionalExperience(ekycData.getProfessionalExperience());
        sysEmployeeInfo.setAccountName(ekycData.getAccountName());
        sysEmployeeInfo.setFullName(ekycData.getFullName());
        sysEmployeeInfo.setAge(ekycData.getAge());
        sysEmployeeInfo.setPhoneNumber(ekycData.getPhoneNumber());
        sysEmployeeInfo.setEmail(ekycData.getEmail());
        sysEmployeeInfo.setManager(ekycData.getManager());
        //地址自动根据身份证获取
        sysEmployeeInfo.setAddress(ekycData.getAddress());
//        sysEmployeeInfo.setAvatarUrl(ekycData.getAvatarUrl());
        sysEmployeeInfo.setCountryCode(ekycData.getCountryCode());
        //待审核
        sysEmployeeInfo.setStatus("1");
        sysEmployeeInfo.setCreateBy(ekycData.getUpdateBy());
        sysEmployeeInfo.setCreateTime(new Date());
        sysEmployeeInfo.setUpdateBy(ekycData.getUpdateBy());
        sysEmployeeInfo.setUpdateTime(new Date());
        return sysEmployeeInfo;
    }


    @Override
    public AgencyEkyc getEkycInfo(Long userId) {
        SysEmployeeInfo queryInfo = new SysEmployeeInfo();
        queryInfo.setUserId(userId);
        List<SysEmployeeInfo> list = sysEmployeeInfoMapper. selectSysEmployeeInfoList(queryInfo);
        if(CollectionUtils.isNotEmpty(list)){
            SysEmployeeInfo sysEmployeeInfo = list.get(0);
            AgencyEkyc agencyEkyc = new AgencyEkyc();
            BeanUtils.copyProperties(sysEmployeeInfo, agencyEkyc);
            SysEmployeeQualifications sysEmployeeQualifications=new SysEmployeeQualifications();
            sysEmployeeQualifications.setEmployeeInfoId(sysEmployeeInfo.getId());
            List<SysEmployeeQualifications> list1 =sysEmployeeQualificationsService.selectSysEmployeeQualificationsList(sysEmployeeQualifications);
            //准换成 List<Qualifications> qualifications;
            List<Qualifications> qualifications = list1.stream().map(item -> {
                Qualifications qualifications1 = new Qualifications();
                BeanUtils.copyProperties(item, qualifications1);
                // 查询资质证书文件
                SysFile sysFile = new SysFile();
                sysFile.setBusinessId(item.getId());
                sysFile.setBusinessType(CertificateFileEnums.CERTIFICATE_FILE.getCode());
                List<SysFile> certificateFiles = iSysFileService.selectSysFileList(sysFile);
                if (CollectionUtils.isNotEmpty(certificateFiles)) {
                    List<String> fileUrls = certificateFiles.stream()
                            .map(SysFile::getFileUrl)
                            .collect(Collectors.toList());
                    qualifications1.setCertificateFileUrls(fileUrls);
                }
                // 查询执业证明文件
                SysFile practiceSysFile = new SysFile();
                practiceSysFile.setBusinessId(item.getId());
                practiceSysFile.setBusinessType(CertificateFileEnums.PRACTICE_CERTIFICATE_FILE.getCode());
                List<SysFile> practiceCertificateFiles = iSysFileService.selectSysFileList(practiceSysFile);
                if (CollectionUtils.isNotEmpty(practiceCertificateFiles)) {
                    List<String> fileUrls = practiceCertificateFiles.stream()
                            .map(SysFile::getFileUrl)
                            .collect(Collectors.toList());
                    qualifications1.setPracticeCertificateFileUrls(fileUrls);
                }
                return qualifications1;
            }).collect(Collectors.toList());
            
            agencyEkyc.setQualifications(qualifications);
            return agencyEkyc;
        }
        return new AgencyEkyc();
    }

    @Override
    public SysEmployeeInfoResp selectSysEmployeeInfo(Long userId) {
        SysEmployeeInfoResp sysEmployeeInfoResp = new SysEmployeeInfoResp();
        SysUser sysUser = userService.selectUserById(userId);
        sysEmployeeInfoResp.setAvatar(sysUser.getAvatar());

        SysEmployeeInfo queryInfo = new SysEmployeeInfo();
        queryInfo.setUserId(userId);
        List<SysEmployeeInfo> list = sysEmployeeInfoMapper. selectSysEmployeeInfoList(queryInfo);
        if(CollectionUtils.isNotEmpty(list)) {
            SysEmployeeInfo sysEmployeeInfo = list.get(0);
            sysEmployeeInfoResp.setUserId(userId);
            sysEmployeeInfoResp.setFullName(sysEmployeeInfo.getFullName());
            //
            sysEmployeeInfoResp.setPosition("代理商");
            sysEmployeeInfoResp.setHireDate(sysUser.getCreateTime());
            //todo 管理的用户
            sysEmployeeInfoResp.setClientCount(0);
            //管理的资产
            sysEmployeeInfoResp.setManagedAssets(new BigDecimal("0"));
            sysEmployeeInfoResp.setEmail(sysEmployeeInfo.getEmail());
            sysEmployeeInfoResp.setPhoneNumber(sysEmployeeInfo.getPhoneNumber());
            sysEmployeeInfoResp.setDeptName("");
            sysEmployeeInfoResp.setLevel("");

            SysEmployeeQualifications sysEmployeeQualifications=new SysEmployeeQualifications();
            sysEmployeeQualifications.setEmployeeInfoId(sysEmployeeInfo.getId());
            List<SysEmployeeQualifications> list1 =sysEmployeeQualificationsService.selectSysEmployeeQualificationsList(sysEmployeeQualifications);
            //准换成 List<Qualifications> qualifications;
            List<Qualifications> qualifications = list1.stream().map(item -> {
                Qualifications qualifications1 = new Qualifications();
                BeanUtils.copyProperties(item, qualifications1);
                // 查询资质证书文件
                SysFile sysFile = new SysFile();
                sysFile.setBusinessId(item.getId());
                sysFile.setBusinessType(CertificateFileEnums.CERTIFICATE_FILE.getCode());
                List<SysFile> certificateFiles = iSysFileService.selectSysFileList(sysFile);
                if (CollectionUtils.isNotEmpty(certificateFiles)) {
                    List<String> fileUrls = certificateFiles.stream()
                            .map(SysFile::getFileUrl)
                            .collect(Collectors.toList());
                    qualifications1.setCertificateFileUrls(fileUrls);
                }
                // 查询执业证明文件
                SysFile practiceSysFile = new SysFile();
                practiceSysFile.setBusinessId(item.getId());
                practiceSysFile.setBusinessType(CertificateFileEnums.PRACTICE_CERTIFICATE_FILE.getCode());
                List<SysFile> practiceCertificateFiles = iSysFileService.selectSysFileList(practiceSysFile);
                if (CollectionUtils.isNotEmpty(practiceCertificateFiles)) {
                    List<String> fileUrls = practiceCertificateFiles.stream()
                            .map(SysFile::getFileUrl)
                            .collect(Collectors.toList());
                    qualifications1.setPracticeCertificateFileUrls(fileUrls);
                }
                return qualifications1;
            }).collect(Collectors.toList());

            sysEmployeeInfoResp.setQualifications(qualifications);
            return sysEmployeeInfoResp;
        }
        return null;
    }
}