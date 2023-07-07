package com.example.demo.repository.rest;

import com.example.demo.entity.Employee;
import com.example.demo.entity.model.PutInfoDto;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    EmployeeRepository repo;

    @GetMapping("")
    public ResponseEntity<List<Employee>> get() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") int id) {
        Optional<Employee> opEm = repo.findById(id);
        if(opEm.isPresent()){
            return ResponseEntity.ok(opEm.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody Employee em){
        Employee newEm = repo.save(em);
        return ResponseEntity.status( HttpStatus.OK ).body(newEm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delById(@PathVariable(name = "id") int id) {
        Optional<Employee> opEm = repo.findById(id);
        if(opEm.isPresent()){
            Employee em = opEm.get();
            if("deleted".equals(em.getStatus())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
            }
            em.setStatus("deleted");
            repo.save(em);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> putById(@RequestBody Employee em){
        if(Objects.isNull(em.getId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ID Input");
        }
        else{
            Integer id = em.getId();
            Optional<Employee> opOldEm = repo.findById(id);
            if(opOldEm.isPresent()){
                Employee oldEm = opOldEm.get();
                oldEm.setFirstName(em.getFirstName());
                oldEm.setLastName(em.getLastName());
                oldEm.setNickName(em.getNickName());
                oldEm.setAddress(em.getAddress());
                Employee newEm = repo.save(oldEm);
                return ResponseEntity.status( HttpStatus.OK ).body(newEm);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
            }
        }
    }

    @PutMapping("/salary/{id}")
    public ResponseEntity<?> putSalaryById(@PathVariable(name = "id") int id, @RequestParam String percent) {
        String num = percent.replace("%","");
        Double per = Double.parseDouble(num);
        if(per<0 || per>100){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BadRequest percent: "+ percent);
        }
        else{
            Optional<Employee> opEm = repo.findById(id);
            if(opEm.isPresent()){
                Employee em = opEm.get();
                Double oldSalary = em.getSalary();
                Double newSalary = oldSalary*(1.0+per/100.0);
                em.setSalary(newSalary);
                Employee newEm = repo.save(em);
                return ResponseEntity.status( HttpStatus.OK ).body(newEm);
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
            }

        }
    }

    @PutMapping("/position/{id}")
    public ResponseEntity<?> putPositionById(@PathVariable(name = "id") int id, @RequestParam String oldPosition,@RequestParam String newPosition) {
        Optional<Employee> opEm = repo.findById(id);
        if(opEm.isPresent()){
            Employee em = opEm.get();
            String curPosition = em.getPosition();
            if(curPosition.equals(oldPosition)){
                em.setPosition(newPosition);
                Employee newEm = repo.save(em);
                return ResponseEntity.status(HttpStatus.OK).body(newEm);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current position is incorrect");
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found employee ID: "+id);
        }
    }

    @GetMapping("/name")
    public ResponseEntity<?> getByName(@RequestParam String q) {
        List<Employee> emList = repo.newQuery(q);
        return ResponseEntity.status(HttpStatus.OK).body(emList);
    }

    @DeleteMapping("")
    public ResponseEntity<?> delBylist(@RequestParam String q) {
        List<Integer> delList = Arrays.stream(q.split(",")).map(s->Integer.parseInt(s)).collect(Collectors.toList());
        List<Integer> notFoundList = new ArrayList<>();
        delList.forEach(id->{
            Optional<Employee> opEm = repo.findById(id);
            if(opEm.isPresent()){
                Employee em = opEm.get();
                em.setStatus("deleted");
                repo.save(em);
//                repo.deleteById(id);
            }else{
                notFoundList.add(id);
            }
        });
        if(notFoundList.size()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }else {
            String nc = notFoundList.stream().map(String::valueOf).collect(Collectors.joining(","));
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body("not_found_ids: ["+nc+"]");
        }

    }

}