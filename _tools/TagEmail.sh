#!/bin/bash
#

stacks="stacks.txt"
email="DCTeamBravo@sabre.com"
stack="dcmc1-ac"

FILES="../cf-as-dev-ecomm-dcmc*/cfn_deploy.json"
Allowed_tagContactEmail="
DCTeamBravo@sabre.com
DCTeamNewton@sabre.com
DCTeamDelta@sabre.com
DCTeamDarwin@sabre.com
dcteamtech@sabre.com
DCTeamAlpha@sabre.com
DCTeamMarvel@sabre.com
DigitalConnect-DevBLR@sabre.com
GPD_SabreSonicPSI@sabre.com
dcreleasemanagers@sabre.com
SSWOperations@sabre.com"

for file in $FILES
do
  stack=$(sed -n '19p' $file)
  email=$(sed -n '24p' $file)
  #stack=$(awk '/ShortName/ {print $2}')
  #email=$(awk '/email/ {print $2}')
  echo "$stack   $email" >> $stacks

  done