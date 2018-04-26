#!/bin/sh
docker run --network=my-net -d -p 50460:50460 --rm --name=backend backend


