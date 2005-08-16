#!/bin/sh
# BeetleJuice wrapper for continuous integreation scripts. 
# (BeetleJuice always uses /bin/sh, so we have to explicitly start /bin/bash)
exec /bin/bash damagecontrol.sh $*
 
