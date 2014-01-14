lui $r0, 0x7f	#most positive num
lui $r1, 0x01	#1

addp8 $r2, $r0, $r1

lui $r0, 0x00
lui $r1, 0x00
ori $r0, $r0, 0x80	#most negative num
ori $r1, $r1, 0xff	#-1

addp8 $r2, $r0, $r1

lui $r0, 0x8f	#most positive num
lui $r1, 0x01	#1

addp8 $r3, $r0, $r1

lui $r0 0x80	#most negative num
lui $r1 0x01	#1

subp8 $r2, $r0, $r1

lui $r0, 0x00
lui $r1, 0x00
lui $r0, 0x07
lui $r1, 0x18
ori $r0, $r0 0x04
ori $r1, $r1 0x04

addp8 $r2, $r0, $r1
subp8 $r3, $r0, $r1



