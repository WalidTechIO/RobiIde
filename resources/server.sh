#!/usr/bin/env bash
bash -c "$(cat server.bat | sed "s/;/:/g")"
