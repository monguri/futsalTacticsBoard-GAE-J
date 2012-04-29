package com.gae.mongry.futsalTacticsBoard;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServiceUser {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id; // プライマリーキー(DataStoreで必要)

	@Persistent
	private String loginId; // ログインID

	@Persistent
	private String mailAddress; // メールアドレス

	@Persistent
	private Date createDate; // 作成日時

	@Persistent
	private Date updateDate; // 更新日時

	public ServiceUser(String loginId, String mailAddress) {
		this.loginId = loginId;
		this.mailAddress = mailAddress;
		Date now = new Date();
		this.createDate = now;
		this.updateDate = now;
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

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
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

}
