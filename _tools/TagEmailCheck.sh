#!/bin/bash
#

stacks="stacks.txt"
sort_stacks="sort_stacks.txt"

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
for email in $Allowed_tagContactEmail
do
if  grep -q $email "$file" ;
then
         echo 'the string exists';
         break
else
         echo 'the string does not exist';
         awk '/ShortName/ {print $2}' $file >> $stacks
fi
done
done
cat $stacks
sort stacks.txt | uniq >> $sort_stacks
cat $sort_stacks
rm $stacks




