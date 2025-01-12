package codegen.spring_angular_auto_generator.springBootGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ZipFileDto {
    private String name;
    private long size;
    private String downloadUrl;
    private Instant lastModifiedDate;

}
