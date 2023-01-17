package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.model.db.File;
import com.example.sparqlmonitoringtool.repository.FileRepository;
import com.example.sparqlmonitoringtool.service.IFileService;
import org.springframework.stereotype.Service;

@Service
public class FileService implements IFileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File getFileByEndpoint(Long endpointId) {
        return fileRepository.findByEndpointId(endpointId);
    }

    @Override
    public File getFileByEndpointAndMimeType(Long endpointId, String format) {
        return fileRepository.findByEndpointIdAndMimeType(endpointId, format);
    }
}
