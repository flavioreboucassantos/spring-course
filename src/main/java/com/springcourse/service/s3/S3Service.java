package com.springcourse.service.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.springcourse.model.UploadedFileModel;

@Service
public class S3Service {
	private AmazonS3 s3;
	private String bucketName;
	private String region;

	@Autowired
	public S3Service(AmazonS3 awsS3, String awsS3BucketName, String awsS3Region) {
		this.s3 = awsS3;
		this.bucketName = awsS3BucketName;
		this.region = awsS3Region;
	}

	public List<UploadedFileModel> upload(MultipartFile[] files) {

		List<UploadedFileModel> uploadedFiles = new ArrayList<UploadedFileModel>();

		for (MultipartFile file : files) {
			String originalName = file.getOriginalFilename();
			String s3Filename = getUniqueFilename(originalName);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());

			// ACL = Access Control List
			try {
				PutObjectRequest request = new PutObjectRequest(bucketName, s3Filename, file.getInputStream(), metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead);

				s3.putObject(request);

				String location = getFileLocation(s3Filename);

				UploadedFileModel uploadedFileModel = new UploadedFileModel(originalName, location);
				uploadedFiles.add(uploadedFileModel);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return uploadedFiles;
	}

	private String getFileLocation(String filename) {
		return "https://" + bucketName + ".s3-" + region + ".amazonaws.com/" + filename;
	}

	private String getUniqueFilename(String filename) {
		return UUID.randomUUID().toString() + "_" + filename;
	}
}
