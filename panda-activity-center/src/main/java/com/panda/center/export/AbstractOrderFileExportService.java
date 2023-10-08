package com.panda.center.export;

import com.panda.center.utils.FtpUtil;
import com.panda.center.utils.ZipFilesUtils;
import com.panda.center.entity.MerchantFile;
import com.panda.center.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  商户订单接口
 * @Date: 2020-12-11 10:40:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public abstract class AbstractOrderFileExportService implements OrderFileExportService {

	@Autowired
	private MerchantFileService merchantFileService;


	@Autowired
	protected FtpProperties ftpProperties;


	public void updateFileStart(Long fileId) {
		merchantFileService.updateFileStatus(fileId);
	}

	public void updateFileStatus(Long fileId, Integer size) {
		merchantFileService.updateFileSizeAndStatus(fileId, size);
	}

	public boolean checkTask(Long id) {
		if (merchantFileService.loadById(id) == null) {
			return true;
		}
		return false;
	}

	public void updateFileStatusEnd(Long fileId) {
		merchantFileService.updateFileEnd(fileId);
	}

	public void exportFail(Long fileId, String remark) {
		merchantFileService.updateFileFail(fileId, remark);
	}

	public void uploadFile(MerchantFile merchantFile, InputStream inputStream) throws IOException {
		InputStream zip = null;
		try {
			zip = ZipFilesUtils.zip(inputStream, merchantFile.getFileName());
			updateFileStatus(merchantFile.getId(), zip.available());
			// ftp上传
			FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), merchantFile.getFtpFileName(), zip);

			updateFileStatusEnd(merchantFile.getId());
		} catch (Exception e) {
			log.error("导出上传异常！", e);
			throw e;
		} finally {
			if (zip != null) {
				zip.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}


}
