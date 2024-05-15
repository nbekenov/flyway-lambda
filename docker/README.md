1. Start the Database
```
docker-compose up -d
```

2. Opne PG-Admin console \
open http://localhost/ in browser and use login/pass from .env.pg_admin file

3. Verify
```
docker ps -a
docker logs <container-id-or-name> --tail 20
```

4. Stop DB
```
docker-compose down
```

5. Clean up
```
docker-compose down --volumes
```
