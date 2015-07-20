package com.sogou.map.logreplay.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sogou.map.logreplay.bean.base.AbstractBean;

@Table(name = "image")
public class Image extends AbstractBean {
	
	public static final String TYPE_SMALL 	= "small";
	public static final String TYPE_MIDDLE 	= "middle";
	public static final String TYPE_LARGE 	= "large";
	public static final String TYPE_RAW 	= "raw";
	
	// TODO 更改为容器级的配置
	public static final String IMAGE_BASE_PATH = "d:/mytest/images/";
	
	@Id
	@Column
	private Long id;
	
	/** 使用sha1算法产生校验和 **/
	@Column
	private String checksum;
	
	/** 图片文件类型 **/
	@Column(name = "format")
	private String format;
	
	/** 图片业务类型 **/
	@Column
	private String type;
	
	/** 图片宽度 **/
	@Column
	private Integer width;
	
	/** 图片长度 **/
	@Column
	private Integer height;
	
	/** 图片文件大小 **/
	@Column
	private Integer size;
	
	/** 创建者id **/
	@Column(name = "creator_id")
	private Long creatorId;
	
	/** 创建时间 **/
	@Column(name = "create_time")
	private Timestamp createTime;
	
	/** 用于数据在程序内部的中转 **/
	@Transient
	private byte[] bytes;
	
	public Image() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@JsonIgnore
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@JsonIgnore
	public String getFilename() {
		return getChecksum() + "." + getFormat();
	}
	
	/**
	 * 文件路径通过生成日期，校验和以及文件类型拼装得到
	 */
	@JsonIgnore
	public String getFilepath() {
		String subDir = DateFormatUtils.format(createTime, "yyyy/MM/dd/");
		return FilenameUtils.concat(IMAGE_BASE_PATH, subDir + getFilename());
	}
	
	public static class Builder {
		
		private String format;
		private byte[] bytes;
		private Long creatorId;
		private int width;
		private int height;
		private String type;
		
		public Builder format(String format) {
			this.format = format;
			return this;
		}
		
		public Builder bytes(byte[] bytes) {
			this.bytes = bytes;
			return this;
		}
		
		public Builder creatorId(Long creatorId) {
			this.creatorId = creatorId;
			return this;
		}
		
		public Builder width(int width) {
			this.width = width;
			return this;
		}
		
		public Builder height(int height) {
			this.height = height;
			return this;
		}
		
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		
		public Image build() {
			Image image = new Image();
			image.setCreatorId(creatorId);
			image.setCreateTime(new Timestamp(System.currentTimeMillis()));
			image.setFormat(format);
			image.setType(type);
			image.setWidth(width);
			image.setHeight(height);
			if(bytes != null) {
				image.setBytes(bytes);
				image.setChecksum(DigestUtils.sha1Hex(bytes));
				image.setSize(bytes.length);
			}
			return image;
		}
		
	}
	
	/** 上传头像时产生的图片尺寸类型 **/
	public enum AvatarType {

		small(32, 32),
		middle(64, 64),
		large(128, 128)
		;
		
		private AvatarType(int width, int height) {
			this.width = width;
			this.height = height;
		}
		
		private int width;
		
		private int height;

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
		
	}
	
}
