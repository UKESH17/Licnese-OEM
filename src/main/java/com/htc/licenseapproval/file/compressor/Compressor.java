package com.htc.licenseapproval.file.compressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.stereotype.Component;

@Component
public class Compressor {
	
	public byte[] compress(byte[] data) throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (GZIPOutputStream gos = new GZIPOutputStream(baos)) {
	        gos.write(data);
	    }
	    return baos.toByteArray();
	}

	
	public byte[] decompress(byte[] compressedData) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
		GZIPInputStream gis = new GZIPInputStream(bais);
		return gis.readAllBytes();
	}

}
