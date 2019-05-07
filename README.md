# deDeups

1. Class Structure:
    app -> App.java (main method)
    service -> Deduplication.java and IDeduplication.java
    model -> Employee.java and LeadsInfo.java

2. Solution:
    In Deduplication.java, there are two hashmap (idMap and emailMap), idMap is stored as <id, employee>, 
    emailMap is stored as <email, employee>.
    
    First read the json file leads.json, and convert the json to LeadsInfo object, after we can get a list of employees.
    then we can loop each employee, compare and insert to idMap and emailMap.
   
    A. When employee`s id doesn`t exist in idMap, and its email doesn`t exist in emailMap, 
       then insert <id, employee> to idMap, <email, employee> to emailMap
   
    B. When employee`s id already exists in idMap, and the date of old record stored in idMap equals or is older than employee`s date. 
       then remove the old record from idMap and emailMap, and insert <id, employee> to idMap, <email, employee> to emailMap.
   
    C. When employee`s email already exists in emailMap while employee`s id doesn`t exist in idMap, and the date of old recrod stored in emaildMap equals or is older than employee`s date.
       then remove the old record from idMap and emailMap, and insert <id, employee> to idMap, <email, employee> to emailMap.
       
3. Generated Json File:
    After we finish the loop, idMap will only store the unique employee objects. Then write it to json file. 
    the generated json file name is newLeads.json
   
4. Logs:
    I print out the md log to console and /logs/deDups.log file.       
