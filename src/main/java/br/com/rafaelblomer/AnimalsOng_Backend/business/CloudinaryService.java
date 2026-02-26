package br.com.rafaelblomer.AnimalsOng_Backend.business;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImagem(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "animalsong",
                            "resource_type", "image"
                    )
            );
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar imagem", e);
        }
    }

    public void deletarPorUrl(String imageUrl) {
        try {
            String publicId = imageUrl
                    .substring(imageUrl.lastIndexOf("/") + 1)
                    .split("\\.")[0];
            cloudinary.uploader().destroy(
                    "animalsong/animais/" + publicId,
                    Map.of()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar imagem", e);
        }
    }

}

