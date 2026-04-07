#!/usr/bin/env python3
"""BX3 Design System for Python - Tkinter/QT/GTK bindings."""

from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name="bx3-design",
    version="1.0.0",
    author="Bxthre3 Inc",
    author_email="dev@bxthre3.com",
    description="BX3 Design System for Python desktop applications",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/bxthre3inc/bx3-integrations",
    packages=find_packages(),
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: Developers",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
        "Programming Language :: Python :: 3.12",
        "Topic :: Software Development :: User Interfaces",
    ],
    python_requires=">=3.8",
    install_requires=[
        "pydantic>=2.0.0",
        "typing-extensions>=4.0.0",
    ],
    extras_require={
        "tkinter": [],
        "qt": ["PyQt6>=6.4.0", "PySide6>=6.4.0"],
        "gtk": ["PyGObject>=3.42.0"],
        "all": ["PyQt6>=6.4.0", "PySide6>=6.4.0", "PyGObject>=3.42.0"],
    },
    entry_points={
        "console_scripts": [
            "bx3-theme=bx3_design.cli:main",
        ],
    },
)