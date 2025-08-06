#!/usr/bin/env bash
set -xe

pushd "$(dirname "$0")"

./gradlew jar
scp build/libs/commitchart-1.0-SNAPSHOT.jar git@git.doomhowl-interactive.com:/home/git/commitchart.jar

USER_SYSTEMD_CONFIG_DIR="/home/git/.config/systemd/user"
ssh git@git.doomhowl-interactive.com "
  systemctl --user stop commitchart.service || true && \
  mkdir -p ${USER_SYSTEMD_CONFIG_DIR}
"

scp commitchart.service git@git.doomhowl-interactive.com:${USER_SYSTEMD_CONFIG_DIR}/commitchart.service

ssh git@git.doomhowl-interactive.com "
  systemctl --user daemon-reload && \
  systemctl enable --now --user commitchart.service && \
  systemctl --user status commitchart.service
"

popd
