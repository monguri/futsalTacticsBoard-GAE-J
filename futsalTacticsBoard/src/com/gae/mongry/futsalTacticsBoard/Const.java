package com.gae.mongry.futsalTacticsBoard;

public class Const {
	// JSON用結果コード
	public static final int RESULT_CD_SUCCESS = 0;
	public static final int RESULT_CD_SYSTEM_ERROR = -1;
	public static final int RESULT_CD_NOT_LOGIN_ERROR = -2;
	public static final int RESULT_CD_NOT_REGIST_ERROR = -3;
	
	// エラーメッセージ
	// できれば、propertiesファイルに外出ししたい
	public static final String ERR_MSG_REGISTED_EMAIL = "Google account is already registed. Please start again from the beginning.";
	public static final String ERR_MSG_INVALID_LOGINID = "Available characters are a-z, A-Z, 0-9 and _.";
	public static final String ERR_MSG_DUPLICATED_LOGINID = "This id is duplicated. Please make other id.";
	public static final String ERR_MSG_DOWNLOAD = "Error occured in downloading. Please try again.";
	public static final String ERR_MSG_SYSTEM = "Sorry. System error occured. Please try again from the beggining.";
	
	// ログインID文字列の最大長
	public static final int LOGIN_ID_MAX_LEN = 12;
	// ログインID文字列の最大長
	public static final String LOGIN_ID_REGEXP = "^[a-zA-Z0-9_]+$";
}
