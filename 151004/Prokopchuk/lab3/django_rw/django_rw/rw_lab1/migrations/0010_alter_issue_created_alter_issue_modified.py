# Generated by Django 4.2.11 on 2024-03-20 09:29

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('rw_lab1', '0009_alter_issue_created_alter_issue_modified_tag'),
    ]

    operations = [
        migrations.AlterField(
            model_name='issue',
            name='created',
            field=models.DateTimeField(default=django.utils.timezone.now),
        ),
        migrations.AlterField(
            model_name='issue',
            name='modified',
            field=models.DateTimeField(default=django.utils.timezone.now),
        ),
    ]
