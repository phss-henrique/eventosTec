package com.eventostec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    public Event createEvent(EventRequestDTO data){
            String imgUrl = null;


            if(data.image() != null){
                imgUrl = this.uploadImg(data.image());
            }

            Event newEvent = new Event();

            newEvent.setDate(new Date(data.date()));
            newEvent.setRemote(data.remote());
            newEvent.setTitle(data.title());
            newEvent.setDescription(data.description());
            newEvent.setEventUrl(data.eventUrl());
            newEvent.setImgUrl(imgUrl);

            return newEvent;

    }
    private String uploadImg(MultipartFile img){
        String imgName = UUID.randomUUID() + "-" + img.getOriginalFilename();

        try{
            File file = this.convertToMultipartFile(img);
            s3Client.putObject(bucketName,imgName,file);
            file.delete();
            return s3Client.getUrl(bucketName,imgName).toString();
        } catch(Exception e){
            System.out.println("Erro ao subir aquivo");
            return null;
        }
    }
    private File convertToMultipartFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}
