#!/usr/bin/env bash
bash -c "$(cat client.bat | sed "s/;/:/g")"
