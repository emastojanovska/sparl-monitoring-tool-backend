package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.db.File;

public interface IFileService {
    File getFileByEndpoint(Long endpointId);
    File getFileByEndpointAndMimeType(Long endpointId, String format);
}
