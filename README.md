![Application Preview](assets/edconv-banner.png)

<div style="text-align: center;">
  <h1>Edconv</h1>

A user-friendly interface that simplifies the power of **FFmpeg**. It's designed for fast and efficient conversion of video and audio files.

  <a href="https://github.com/edneyosf/Edconv/releases"><img src="https://img.shields.io/github/v/release/edneyosf/Edconv"/></a>
  <a href="https://github.com/edneyosf/Edconv/releases"><img src="https://img.shields.io/github/downloads/edneyosf/Edconv/total"/></a>

  <a href="https://github.com/edneyosf/Edconv/actions/workflows/linux-build.yml"><img src="https://github.com/edneyosf/Edconv/actions/workflows/linux-build.yml/badge.svg"/></a>
  <a href="https://github.com/edneyosf/Edconv/actions/workflows/windows-build.yml"><img src="https://github.com/edneyosf/Edconv/actions/workflows/windows-build.yml/badge.svg"/></a>

  <h4>Download</h4> 
  <a href="https://github.com/edneyosf/Edconv/releases">
    <img src="assets/badge_github.png" height="80">
  </a>
</div>

## Features ✨

- Convert video and audio using FFmpeg
- Custom FFmpeg arguments
- Console mode (viewing logs at runtime)
- Queue
- Media Information
- VMAF, PSNR and SSIM perceptual video quality assessment algorithm

## Screenshots

![Application Preview](assets/edconv.png)

## Requirements

[FFmpeg](https://ffmpeg.org/download.html) must be installed and accessible in your system, we also provide GPL-licensed binaries in our releases, **recommended to use version 7.1 or higher**.

On first launch, Edconv will prompt you to select your local FFmpeg binary.

## Installation

Download the latest release for your system from the [Releases page](https://github.com/edneyosf/edconv/releases):

### Windows:

Run the installer as administrator and follow the installation steps.

### AppImage

Before running the AppImage, ensure it has execution permissions. Open a terminal and run:

```bash
chmod +x edconv-x.x.x-x86_64.AppImage
```

Run the AppImage from the terminal:

```bash
./edconv-x.x.x-x86_64.AppImage
```

### Debian-based systems:  
```bash
sudo dpkg -i edconv_x.x.x.deb
```

### RPM-based systems:
```bash
sudo dnf install edconv_x.x.x.rpm
```

## Support & Donations

Special thanks to all supporter ❤️

<a href="https://buymeacoffee.com/edneyosf">
  <img alt="" src="assets/bmc-button.svg" width="200">
</a>
