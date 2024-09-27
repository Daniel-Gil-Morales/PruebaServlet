package Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    private int id;

    private String code;

    private String name;


    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
