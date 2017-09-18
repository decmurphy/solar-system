set key off
set term png
set output 'images/full.png'

#######

set view equal xyz
splot 'output/Sun.dat' u 2:3:4 w l, 'output/Mercury.dat' u 2:3:4 w l, 'output/Venus.dat' u 2:3:4 w l, 'output/Earth.dat' u 2:3:4 w l, 'output/Luna.dat' u 2:3:4 w l, 'output/Mars.dat' u 2:3:4 w l, 'output/Jupiter.dat' u 2:3:4 w l, 'output/Saturn.dat' u 2:3:4 w l, 'output/Uranus.dat' u 2:3:4 w l, 'output/Neptune.dat' u 2:3:4 w l, 'output/Pluto.dat' u 2:3:4 w l

#### OR

#splot 'output/Sun.dat' u 1:2:3 w l, 'output/Mercury.dat' u 1:2:3 w l, 'output/Venus.dat' u 1:2:3 w l, 'output/Earth.dat' u 1:2:3 w l, 'output/Luna.dat' u 1:2:3 w l, 'output/Mars.dat' u 1:2:3 w l, 'output/Jupiter.dat' u 1:2:3 w l, 'output/Saturn.dat' u 1:2:3 w l, 'output/Uranus.dat' u 1:2:3 w l, 'output/Neptune.dat' u 1:2:3 w l, 'output/Pluto.dat' u 1:2:3 w l

#######
