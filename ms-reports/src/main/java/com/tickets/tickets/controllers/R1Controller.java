import com.tickets.tickets.entities.R1Entity;
import com.tickets.tickets.services.R1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/r1")
public class R1Controller {

    @Autowired
    private R1Service r1Service;

    @GetMapping("/{id}")
    public ResponseEntity<R1Entity> getR1ById(@PathVariable Long id) {
        R1Entity r1Entity = r1Service.getR1ById(id);
        if (r1Entity != null) {
            return ResponseEntity.ok(r1Entity);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<R1Entity> saveR1(@RequestBody R1Entity r1) {
        R1Entity savedR1 = r1Service.saveR1(r1);
        if (savedR1 != null) {
            return ResponseEntity.ok(savedR1);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteR1(@PathVariable Long id) {
        r1Service.deleteR1(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<R1Entity> updateR1(@RequestBody R1Entity r1) {
        R1Entity updatedR1 = r1Service.updateR1(r1);
        if (updatedR1 != null) {
            return ResponseEntity.ok(updatedR1);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllR1() {
        r1Service.deleteAllR1();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeValues() {
        r1Service.initializeValues();
        return ResponseEntity.ok().build();
    }
}