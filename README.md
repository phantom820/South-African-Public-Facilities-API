# South-African-Public-Facilities-API
This project aims to build an API that can provide information about public facilities such as Schools, Hospitals , Universities etc.Note this is not the ground truth for any of the information and the data might be stale since currently scrapped , this came from the struggle of trying to find such information in another project is still quite experimental. More documentation to be added as we go 

### What's Required 
To be able to run this locally and make a few test API calls ally you need is `docker` and `docker-compose` . After installing these run the following from the root of the repository

```
 docker-compose up --build
```

this should spin up the follow  
- Spring Boot Web Server ()
- Prometheus (`http://localhost:9090/graph?g0.expr=&g0.tab=1&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h`)
  - `http://localhost:8080/actuator/prometheus`  
- Grafana (`http://localhost:3000/login`)


### Playground 
Everything is currently under development but there are a few test resources alread (Schools and Hospitals)

#### School 
##### Endpoints 
- `http://localhost:8080/schools`
- `http://localhost:8080/schools/{schooldId}` i.e `http://localhost:8080/schools/918510576`
- `http://localhost:8080/schools/918510576?filter-key-1=province&filter-key-1-value=Mpumalanga`
```
{
  "schoolId": "100000038",
  "name": "Silvermine Academy",
  "status": "Operational",
  "sector": "Independent",
  "type": "Ordinary School",
  "phase": "Secondary School",
  "specialization": "Ordinary School",
  "examNo": "0",
  "province": "Western Cape",
  "districtMunicipality": "City Of Cape Town Metropolitan Municipality",
  "localMunicipality": "City Of Cape Town Metropolitan Municipality",
  "postalCode": "7975.0"
}
```


#### Hospital 
##### Endpoints 
- `http://localhost:8080/hospitals`
- `http://localhost:8080/hospitals/{hospitalId}` i.e `http://localhost:8080/hospitals/1`
- `http://localhost:8080/hospitals?filter-key-1=province&filter-key-1-value=Mpumalanga`
```
{
  "hospitalId": 158,
  "name": "Kriel medical clinic",
  "category": "Private Hospital",
  "province": "Mpumalanga",
  "district": "Nkangala District Municipality"
},
```
