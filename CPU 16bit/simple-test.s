addi $r0 $r0 0x01
disp $r0 0
addi $r1 $r1 0x02
disp $r1 1
add $r2 $r0 $r1
sub $r0 $r2 $r1
or $r0 $r1 $r2
addi $r1 $r3 0x00
and $r2 $r0 $r1
