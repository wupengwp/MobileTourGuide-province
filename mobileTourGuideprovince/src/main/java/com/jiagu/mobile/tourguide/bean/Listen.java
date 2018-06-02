package com.jiagu.mobile.tourguide.bean;

public class Listen {
	private String attractitle;
	private String attracid;
	private String scenictitle;
	private String type;
	private String listenCount;
	private String tourid;
	private String url;
	private String id;
	private String characteristicTitle;
	private String characteristicId;
	private String timer;
	private String voiceUrl;
	private String scenicid;
	private String scenicPriId;
	private Boolean checked = false;
	public Listen(String attractitle, String attracid, String scenictitle,
			String type, String listenCount, String tourid, String url,
			String id, String characteristicTitle, String characteristicId,
			String timer, String voiceUrl, String scenicid, String scenicPriId,
			Boolean checked) {
		super();
		this.attractitle = attractitle;
		this.attracid = attracid;
		this.scenictitle = scenictitle;
		this.type = type;
		this.listenCount = listenCount;
		this.tourid = tourid;
		this.url = url;
		this.id = id;
		this.characteristicTitle = characteristicTitle;
		this.characteristicId = characteristicId;
		this.timer = timer;
		this.voiceUrl = voiceUrl;
		this.scenicid = scenicid;
		this.scenicPriId = scenicPriId;
		this.checked = checked;
	}
	public Listen() {
		super();
	}
	public String getAttractitle() {
		return attractitle;
	}
	public void setAttractitle(String attractitle) {
		this.attractitle = attractitle;
	}
	public String getAttracid() {
		return attracid;
	}
	public void setAttracid(String attracid) {
		this.attracid = attracid;
	}
	public String getScenictitle() {
		return scenictitle;
	}
	public void setScenictitle(String scenictitle) {
		this.scenictitle = scenictitle;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getListenCount() {
		return listenCount;
	}
	public void setListenCount(String listenCount) {
		this.listenCount = listenCount;
	}
	public String getTourid() {
		return tourid;
	}
	public void setTourid(String tourid) {
		this.tourid = tourid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCharacteristicTitle() {
		return characteristicTitle;
	}
	public void setCharacteristicTitle(String characteristicTitle) {
		this.characteristicTitle = characteristicTitle;
	}
	public String getCharacteristicId() {
		return characteristicId;
	}
	public void setCharacteristicId(String characteristicId) {
		this.characteristicId = characteristicId;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getVoiceUrl() {
		return voiceUrl;
	}
	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	public String getScenicid() {
		return scenicid;
	}
	public void setScenicid(String scenicid) {
		this.scenicid = scenicid;
	}
	public String getScenicPriId() {
		return scenicPriId;
	}
	public void setScenicPriId(String scenicPriId) {
		this.scenicPriId = scenicPriId;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	@Override
	public String toString() {
		return "Listen [attractitle=" + attractitle + ", attracid=" + attracid
				+ ", scenictitle=" + scenictitle + ", type=" + type
				+ ", listenCount=" + listenCount + ", tourid=" + tourid
				+ ", url=" + url + ", id=" + id + ", characteristicTitle="
				+ characteristicTitle + ", characteristicId="
				+ characteristicId + ", timer=" + timer + ", voiceUrl="
				+ voiceUrl + ", scenicid=" + scenicid + ", scenicPriId="
				+ scenicPriId + ", checked=" + checked + "]";
	}
	
}
