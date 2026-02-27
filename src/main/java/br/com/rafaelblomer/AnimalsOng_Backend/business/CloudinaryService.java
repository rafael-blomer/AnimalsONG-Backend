package br.com.rafaelblomer.AnimalsOng_Backend.business;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImagem(MultipartFile file) {
        log.info("Upload iniciado — arquivo: {}, tamanho: {} bytes, tipo: {}",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType());

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "animalsong",
                            "resource_type", "image",
                            "format", "jpg"
                    )
            );
            String url = result.get("secure_url").toString();
            log.info("Upload concluído: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Erro de I/O no upload — tipo: {}, tamanho: {}",
                    file.getContentType(), file.getSize(), e);
            throw new RuntimeException("Erro ao enviar imagem", e);
        } catch (Exception e) {
            log.error("Erro inesperado no upload — tipo: {}, tamanho: {}",
                    file.getContentType(), file.getSize(), e);
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