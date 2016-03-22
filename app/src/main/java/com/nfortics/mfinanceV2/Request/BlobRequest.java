package com.nfortics.mfinanceV2.Request;


import com.nfortics.mfinanceV2.Utilities.Utils;

public class BlobRequest implements Request {

	// private static final String BASE_URL = Application.serverURL3 +
	// "/get_blob.php?";
	private String url;
	private final String avatarId;
	private final Integer width;
	private final Integer height;

	public BlobRequest(String url, Integer width, Integer height) {
		this.avatarId = Utils.getMd5Hash(url);
		this.width = width;
		this.height = height;
		buildURL(url);
	}

	@Override
	public String getURL() {
		return url;
	}

	private void buildURL(String url) {
		// StringBuilder builder = new StringBuilder(BASE_URL);
		// addId(blobId, builder);
		this.url = url.toString();
	}

	public void addId(String id, StringBuilder urlBuilder) {
		urlBuilder.append("image_id=");
		urlBuilder.append(id);
	}

	public String getAvatarId() {
		return avatarId;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

}