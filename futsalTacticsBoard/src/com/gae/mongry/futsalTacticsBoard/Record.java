package com.gae.mongry.futsalTacticsBoard;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Record {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id; // プライマリーキー(DataStoreで必要)

	@Persistent
	private String loginId; // ログインID

	@Persistent
	private Date createDate; // 作成日時

	@Persistent
	private Date updateDate; // 更新日時

	@Persistent
	private String title; // 録画データ

	@Persistent
	private String comment; // 録画データ

	@Persistent
	private Text data; // 録画データ

	public Record(String loginId, Date now, String title, String comment, Text dataText) {
		this.loginId = loginId;
		this.createDate = now;
		this.updateDate = now;
		this.title = title;
		this.comment = comment;
		this.data = dataText;
	}

	public Long getId() {
		return id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Text getData() {
		return data;
	}

	public void setData(Text data) {
		this.data = data;
	}

}
